## Experiment MTLS with node.js server and wget client
source: 
* https://codeburst.io/mutual-tls-authentication-mtls-de-mystified-11fa2a52e9cf
* https://github.com/joutwate/mtls-springboot
* https://curity.io/resources/tutorials/howtos/writing-clients/oidc-spring-boot-mtls-auth/
### create CA certificate
```
openssl req ^
  -new ^
  -x509 ^
  -nodes ^
  -days 365 ^
  -subj "/CN=saber-ca" ^
  -keyout ca.key ^
  -out ca.crt
```
* to verify the certificates created
```
openssl x509 ^
  --in ca.crt ^
  -text ^
  --noout
```
### Create server key and certificate
* create private key
```
openssl genrsa ^
  -out server.key 2048
```
* create signing request
```
openssl req ^
  -new ^
  -key server.key ^
  -subj "/CN=localhost" ^
  -out server.csr
```
* create certificate
```
openssl x509 ^
  -req ^
  -in server.csr ^
  -CA ca.crt ^
  -CAkey ca.key ^
  -CAcreateserial ^
  -days 365 ^
  -out server.crt
```
* to verify the certificates created
```
openssl x509 ^
  --in server.crt ^
  -text ^
  --noout
```
### Create client key and certificate
* create private key
```
openssl genrsa ^
  -out client.key 2048
```
* create signing request
```
openssl req ^
  -new ^
  -key client.key ^
  -subj "/CN=the-client" ^
  -out client.csr
```
* create certificate
```
openssl x509 ^
  -req ^
  -in client.csr ^
  -CA ca.crt ^
  -CAkey ca.key ^
  -CAcreateserial ^
  -days 365 ^
  -out client.crt
```
* to verify the certificates created
```
openssl x509 ^
  --in client.crt ^
  -text ^
  --noout
```
### Start the server
```
node server.js
```
### Initiate call from the client
* provide client certificate
```
wget --ca-cert ca.crt --certificate=client.crt --private-key=client.key -O- https://localhost:3000
```
* without providing the certificates
```
wget --ca-cert ca.crt -O- https://localhost:3000
```
## Setup mtls enabled spring boot server
### Create server jks key store
* Generate the certificate in PKC12 format 
```
openssl pkcs12 -export ^
               -in server.crt ^
               -inkey server.key ^
               -out server.p12 ^
               -name server ^
               -CAfile ca.crt ^
               -caname saber-ca
```
for the password the value used is: ```qwerty```
* Generate jks file
```
keytool -importkeystore ^
        -deststorepass 98253915 ^
        -destkeypass 98253915 ^
        -destkeystore server.jks ^
        -srckeystore server.p12 ^
        -srcstoretype PKCS12 ^
        -srcstorepass qwerty ^
        -alias server
```
* add CA certificate
```
keytool -importcert ^
        -file ca.pem ^
        -alias saber-ca ^
        -keystore server.jks ^
        -storepass 98253915 ^
        -noprompt
```
* Read content ok the keystore file
```
keytool -list -v -keystore server.jks
```
for password use ```98253915```
### Create client jks key store
* Generate the certificate in PKC12 format
```
openssl pkcs12 -export ^
               -in client.crt ^
               -inkey client.key ^
               -out client.p12 ^
               -name client ^
               -CAfile ca.crt ^
               -caname saber-ca
```
for the password the value used is: ```qwerty```
* Generate jks file
```
keytool -importkeystore ^
        -deststorepass 98253915 ^
        -destkeypass 98253915 ^
        -destkeystore client.jks ^
        -srckeystore client.p12 ^
        -srcstoretype PKCS12 ^
        -srcstorepass qwerty ^
        -alias client
```
* add CA certificate
```
keytool -importcert ^
        -file ca.pem ^
        -alias saber-ca ^
        -keystore client.jks ^
        -storepass 98253915 ^
        -noprompt
```
* Read content ok the keystore file
```
keytool -list -v -keystore client.jks
```
for password use ```98253915```
## Integrate key-vault
* generate server side pem
```
openssl pkcs12 -in server.p12 -out server.pem
```