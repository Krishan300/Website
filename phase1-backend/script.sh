docker run -p5432:5432 --name phase0-db -e POSTGRES_USER=root -e POSTGRES_PASS=abc123 -e POSTGRES_DB=mydb -d postgres:latest

POSTGRES_IP=127.0.0.1 POSTGRES_PORT=5432 POSTGRES_DB=mydb POSTGRES_USER=root POSTGRES_PASS=abc123 mvn exec:java