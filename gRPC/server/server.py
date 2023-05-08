from concurrent import futures
import random
import time
import grpc
import os
import event_pb2 as e_pb2
import event_pb2_grpc as e_pb2_grpc

INTERVAL = 1
CITIES = ["Warsaw", "Viena", "Berlin", "Paris", "London"]
EVENT = ["Concert", "Festival", "Theatre", "Cinema", "Exhibition"]

SIZES = [e_pb2.SMALL, e_pb2.MEDIUM, e_pb2.LARGE]
RATINGS = [e_pb2.Rating(value=1),e_pb2.Rating(value=1), e_pb2.Rating(value=5), e_pb2.Rating(value=10)]


class Event:
    def __init__(self):
        self.events = {}
        for city, event in zip(CITIES, EVENT):
            self.events[city] = {
                "name": city,
                "city": city,
                "size": random.choice(SIZES),
                "ratings": RATINGS,
            }

    def get_event(self, city):
        if city not in self.events:
            return None
        event = self.events[city]
        event["size"] = random.choice(SIZES)
        event["ratings"] = [e_pb2.Rating(value=random.randint(1, 10)) for _ in range(random.randint(3, 5))]
        event_response = e_pb2.Event(name=event["name"], city=event["city"], size=event["size"],
                                     ratings=event["ratings"])
        return event_response


def validate_cities(cities, context):
    for city in cities:
        if city not in CITIES:
            context.set_details(f"City with name '{city}' does not exist")
            context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
            return True
    return False


def validate_interval(interval, context):
    if interval < 1 or interval > 60 * 60:
        context.set_details(
            f"Interval {interval} is not valid, Interval must be between 1 second and 1 hour")
        context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
        return True
    return False

def validate_rating(rating, context):
    if rating < 0 or rating > 10:
        context.set_details(
            f"Rating {rating} is not valid, Rating must be between 0 (default) and 10")
        context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
        return True
    return False

def validate_size(size, context):
    if size < 0 or size > 4:
        context.set_details(
            f"Size {size} is not valid, Size must be between 0 (default) and 4 'SMALL' 'MEDIUM' 'LARGE'")
        context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
        return True
    return False


class EventNotifier(e_pb2_grpc.EventService):
    def __init__(self):
        self.event = Event()

    def SubscribePeriodic(self, request, context):
        print(f"Received periodic subscription request with interfal {request.interval} and peer {context.peer()}")
        if validate_cities(request.cities, context):
            return e_pb2.Event()

        if validate_interval(request.interval, context):
            return e_pb2.Event()

        context.add_callback(lambda: print(f"Connection lost, peer: {context.peer()}"))

        que = []
        while True:
            for city in request.cities:
                event = self.event.get_event(city)
                if event is not None:
                    que.append(event)

            if context.is_active():
                while len(que) > 0:
                    yield que.pop(0)
                    print(f"Sending event to peer {context.peer()}")
                time.sleep(request.interval)

    def SubscribeOnCondition(self, request, context):
        print(f"Received on condition subscription request with peer {context.peer()}")
        print(request.cities, request.size, request.rating)
        if validate_cities(request.cities, context):
            return e_pb2.Event()
        if validate_size(request.size, context):
            return e_pb2.Event()
        if validate_rating(request.rating.value, context):
            return e_pb2.Event()

        context.add_callback(lambda: print(f"Connection lost, peer: {context.peer()}"))

        que = []

        while True:
            for city in request.cities:
                event = self.event.get_event(city)
                # Check whether the event matches the specified conditions
                if (not hasattr(request, "size") or request.size == 0 or event.size == request.size):
                    rating_ok = False
                    for rating in event.ratings:
                        if rating == request.rating.value:
                            rating_ok = True
                            break
                    rating_ok = rating_ok or not hasattr(request, "rating") or request.rating.value == 0
                    if rating_ok:
                        que.append(event)

            if context.is_active():
                while len(que) > 0:
                    yield que.pop(0)
                    print(f"Sending event to peer {context.peer()}")

            time.sleep(INTERVAL)


def serve():
    port = os.getenv("SERVER_PORT", "50051")
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    e_pb2_grpc.add_EventServiceServicer_to_server(EventNotifier(), server)
    server.add_insecure_port(f"[::]:{port}")
    server.start()
    print(f"Server started on port {port}")
    server.wait_for_termination()


if __name__ == "__main__":
    serve()
