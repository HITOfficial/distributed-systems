import {loadPackageDefinition, credentials} from "grpc";
import {loadSync} from "@grpc/proto-loader"
import path from "path";
import {createInterface} from "readline";
import {fileURLToPath} from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const packageDefinition = loadSync(path.resolve(__dirname, "../protos/event.proto"), {
    keepCase: true,
    longs: String,
    enums: String,
    defaults: true,
    oneofs: true
});

const eventPackage = loadPackageDefinition(packageDefinition).eventPackage;


const validateArgs = (args, argsNumber) => {
    if (args.length < argsNumber) {
        console.error(`Expected at least ${argsNumber} arguments, received ${args.length}`)
        return false;
    }
    return true;
}

const handleCall = (call) => {
    call.on("data", (event) => {
        console.log("Received event: ", JSON.stringify(event))
    })
    call.on("end", () => {
        console.log("Subscription ended")
    })
    call.on("error", (err) => {
        console.error(`Error Code ${err.code} ${err.details} ` )
    })
}

const main = () => {
    const client = new eventPackage.EventService("localhost:50051", credentials.createInsecure());
    const rl = createInterface({
        input: process.stdin,
        output: process.stdout,
        terminal: false
    });

    const calls = []
    console.log("Client connected to server")
    console.log("Available commands: \n ")
    console.log(`periodic <interval> <cities> - subscribe to periodic events from cities with given interval`)
    console.log(`conditional <size / rating> <value> <cities> - subscribe to conditional events from cities with given interval`)

    rl.on("line", (line) => {
        let sub = {}
        const [method, ...args] = line.split(" ");
        switch (method) {
            case "periodic":
                // interval cities
                if (!validateArgs(args, 2)) return;
                const [interval, ...cities] = args;
                sub = {interval: parseInt(interval), cities: cities}
                console.log(sub)
                calls.push(client.subscribePeriodic(sub))
                handleCall(calls.at(-1))
                console.log(`Subscription at index ${calls.length - 1}`)
                break;
            case "conditional":
                // condition, size / rating, cities
                console.log(args)
                if (!validateArgs(args, 3)) return;
                const [condition, value, ...rest] = args;
                switch (condition) {
                    case "size":
                        sub = {size: parseInt(value), cities: rest}
                        break;
                    case "rating":
                        sub = {rating: {value: parseInt(value)}, cities: rest}
                        break;
                    default:
                        console.log("Invalid condition type ")
                        return;
                }
                console.log(sub)

                calls.push(client.subscribeOnCondition(sub))
                handleCall(calls.at(-1))
                console.log(`Subscription nr ${calls.length - 1}`)

                break;
            case "cancel":
                const idx = args[0]
                if (!validateArgs(args, 1)) return;
                if (calls.length <= idx) return console.log("dont have that much subscriptions");
                calls.at(parseInt(idx)).cancel()
                calls.splice(idx,1)
                console.log("Cancelled subscription: " + idx)
            default:
                console.log("Invalid command")
        }
    })
}

main();