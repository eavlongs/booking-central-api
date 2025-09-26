## Setup for JWT Authentication
Since this project is using JWT for authentication, using RS256 algorithm, you need to set up your RSA keys, and adjust the path in application.properties appropriately.

The following commands are used to generate the private key:

```bash
openssl genpkey -algorithm RSA -out ./src/main/resources/private_key.pem
```

Then, to generate the corresponding public key, use:
```bash
openssl rsa -pubout -in ./src/main/resources/private_key.pem -out ./src/main/resources/public_key.pem
```

And make sure that in application.properties, you have the correct paths set:
```properties
# private key location
jwt.key.private=private_key.pem
# public key location
jwt.key.public=public_key.pem
```