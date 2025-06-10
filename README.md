
# Create and run the app as a docker image

### Prerequisites: docker
- Open a terminal in the parent folder path
- Run the following command:
  - docker build -t crypto-1.0.0 .

- Create a folder [data] under the root directory, if there is not an existing one, and drop the csd files having the naming
pattern: CRYPTO_NAME_values.csv and the format [timestamp, symbol, price]
- Run the following command:
  - docker run -p 8080:8080 -v ${PWD}/data:/app/data crypto-1.0.0


# Run app from the command line

- mvn clean package
- java -jar .\target\crypto-1.0.0.jar