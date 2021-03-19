# kinesis-sender

# This is small program that can be used for load test of AWS Kinesis DataFirehose
```
docker build -t almerico/kinesis-sender .

docker run  \                        
 --env AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY \
 --env AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID \
 --env AWS_DEFAULT_REGION=$AWS_DEFAULT_REGION \
 --env NUMBER_SENDER_THREADS=2 \
 --env NUMBER_SENDER_RECORDS=500 \
 --env DELIVERY_STREAM_NAME=loadtest \
 --env SENDER_DEBUG_IS_ON=false almerico/kinesis-sender 
 ```
