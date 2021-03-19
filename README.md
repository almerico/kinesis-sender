# kinesis-sender

# This is small program that can be used for load test of AWS Kinesis DataFirehose
```
docker build -t almerico/kinesis-sender .

docker run  --env AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY --env AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID  --env AWS_DEFAULT_REGION=us-east-1  --env NUMBER_SENDER_THREADS=100  --env NUMBER_SENDER_RECORDS=1 --env DELIVERY_STREAM_NAME=loadtest  --env SENDER_DEBUG_IS_ON=false --env NUMBER_SENDER_SENDS=100 --env MILISECONDS_BETWEEN_SEND=100 almerico/kinesis-sender


 ```
Main idea is to emulate a lot of clients which periodically send some data to kinesis.
