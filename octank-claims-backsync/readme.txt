

   1. Create a package: mvn package

   2. Upload to S3: mvn install

   3. Deploy Lambda: aws cloudformation deploy --template-file app.yml --stack-name claimsbacksync
   
   3b. aws lambda update-function-code --function-name ClaimsBacksync --s3-bucket octank-healthcare --s3-key octank-claims-backsync-1.0-SNAPSHOT.jar

   4. Invoke Lambda:

   5. aws lambda invoke \
    --function-name ClaimsBacksync \
    --payload '{ "requestId": "batch-claims", "claimStatus": "Submitted" }' \
    claims.out

   6. Delete Lambda: aws cloudformation delete-stack --stack-name claimsbacksync

