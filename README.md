# pavilion-backend

Pavilion backend application. Currently a single AWS Lambda handler serving `GET /hello`, deployed via Terraform from the [pavilion-infra](https://github.com/project-pavilion/pavilion-infra) repo.

## Prerequisites

- JDK 21
- Maven 3.9+

## Build & test

```bash
mvn test      # run unit tests
mvn package    # build the deployable uber-jar (maven-shade-plugin)
```

`mvn package` produces `target/pavilion-backend-1.0-SNAPSHOT.jar`, a self-contained jar with all dependencies bundled (via `maven-shade-plugin`). This is the artifact `pavilion-infra` points Terraform at (`filename`/`source_code_hash` in `lambda.tf`).

## Handler

`com.pavilion.hello.HelloHandler` implements `RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>` and returns:

```json
{"message": "Hello from Lambda"}
```

Configured Lambda handler string: `com.pavilion.hello.HelloHandler::handleRequest`.

## Testing the built jar locally (no AWS required)

You can run the exact built jar inside AWS's own Lambda Java runtime image using the Lambda Runtime Interface Emulator (RIE), entirely locally.

1. Install a Docker runtime. On macOS without Docker Desktop, [Colima](https://github.com/abiosoft/colima) works and is fully CLI-driven:
   ```bash
   brew install colima docker
   colima start
   ```
2. Build the jar: `mvn package`
3. Run it in the Lambda base image. **Important:** the jar must be mounted at `/var/task/lib/`, not `/var/task/` directly — the base image only adds `/var/task/lib/*.jar` to its classpath.
   ```bash
   mkdir -p target/lib-mount
   cp target/pavilion-backend-1.0-SNAPSHOT.jar target/lib-mount/

   docker run -d --rm --name pavilion-hello-local \
     -p 9000:8080 \
     -v "$(pwd)/target/lib-mount:/var/task/lib" \
     public.ecr.aws/lambda/java:21 \
     com.pavilion.hello.HelloHandler::handleRequest
   ```
4. Invoke it:
   ```bash
   curl -s -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{}'
   # {"statusCode":200,"headers":{"Content-Type":"application/json"},"body":"{\"message\": \"Hello from Lambda\"}","isBase64Encoded":false}
   ```
5. Clean up:
   ```bash
   docker rm -f pavilion-hello-local
   rm -rf target/lib-mount
   ```

## Deploying

Infrastructure (Lambda function, IAM role, API Gateway) is provisioned entirely from the separate [pavilion-infra](https://github.com/project-pavilion/pavilion-infra) repo, which expects this repo to be checked out as a sibling directory and reads the built jar from `../pavilion-backend/target/pavilion-backend-1.0-SNAPSHOT.jar`. See that repo's README for deploy instructions.
