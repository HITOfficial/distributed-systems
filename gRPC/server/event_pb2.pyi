from google.protobuf.internal import containers as _containers
from google.protobuf.internal import enum_type_wrapper as _enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DEFAULT: Size
DESCRIPTOR: _descriptor.FileDescriptor
LARGE: Size
MEDIUM: Size
SMALL: Size

class Empty(_message.Message):
    __slots__ = []
    def __init__(self) -> None: ...

class Event(_message.Message):
    __slots__ = ["city", "name", "ratings", "size"]
    CITY_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    RATINGS_FIELD_NUMBER: _ClassVar[int]
    SIZE_FIELD_NUMBER: _ClassVar[int]
    city: str
    name: str
    ratings: _containers.RepeatedCompositeFieldContainer[Rating]
    size: Size
    def __init__(self, name: _Optional[str] = ..., city: _Optional[str] = ..., size: _Optional[_Union[Size, str]] = ..., ratings: _Optional[_Iterable[_Union[Rating, _Mapping]]] = ...) -> None: ...

class OnConditionSubscription(_message.Message):
    __slots__ = ["cities", "rating", "size"]
    CITIES_FIELD_NUMBER: _ClassVar[int]
    RATING_FIELD_NUMBER: _ClassVar[int]
    SIZE_FIELD_NUMBER: _ClassVar[int]
    cities: _containers.RepeatedScalarFieldContainer[str]
    rating: Rating
    size: Size
    def __init__(self, cities: _Optional[_Iterable[str]] = ..., size: _Optional[_Union[Size, str]] = ..., rating: _Optional[_Union[Rating, _Mapping]] = ...) -> None: ...

class PeriodicSubscription(_message.Message):
    __slots__ = ["cities", "interval"]
    CITIES_FIELD_NUMBER: _ClassVar[int]
    INTERVAL_FIELD_NUMBER: _ClassVar[int]
    cities: _containers.RepeatedScalarFieldContainer[str]
    interval: int
    def __init__(self, interval: _Optional[int] = ..., cities: _Optional[_Iterable[str]] = ...) -> None: ...

class Rating(_message.Message):
    __slots__ = ["value"]
    VALUE_FIELD_NUMBER: _ClassVar[int]
    value: int
    def __init__(self, value: _Optional[int] = ...) -> None: ...

class Size(int, metaclass=_enum_type_wrapper.EnumTypeWrapper):
    __slots__ = []
