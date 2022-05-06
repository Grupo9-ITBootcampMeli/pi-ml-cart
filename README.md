# Cart API

Cart API is a REST API service to manage all marketplace needs in the project scope. It was developed during IT Bootcamp - Wave 5 as a part of the last group project.

## Endpoints

See also the OpenAPI Specification (/swagger-ui.html while running).

- Base API Port: 8082
- Base Database Port: 5434

| Method   | URI       | Description    |
| :---------- | :--------- | :----------------------- |
| `POST` | `/api/v1/fresh-products/orders/` | Register a new Cart |
| `GET` | `/api/v1/fresh-products/orders/` | List products on Cart |
| `PUT` | `/api/v1/fresh-products/orders/` | Update Cart status |
| `POST` | `/buyer/v1/` | Register a new Buyer |
| `GET` | `/buyer/v1/{id}` | Find Buyer by ID |

## Requirements

- Java 11 and later

## Installation and Usage

Use the given Maven Wrapper and Docker to build a new service container

```bash
## 1. Clone project to local 
git clone https://github.com/Grupo9-ITBootcampMeli/pi-ml-cart

## 2. Use Maven Wrapper to generate a new build  
./mvnw clean package

## 2.1. Optionally, build without tests 
./mvnw clean package -DskipTests

## 3. Start service via Docker 
docker-compose up

## 3.1. If you are recreating a container, build a new Docker image and delete the previous
docker-compose build --no-cache && docker-compose up -d && docker rmi -f $(docker images -f "dangling=true" -q)

```

For full operation, it is necessary to have the [Gandalf](https://github.com/Grupo9-ITBootcampMeli/pi-ml-gandalf), [Products](https://github.com/Grupo9-ITBootcampMeli/pi-ml-products) and [Warehouse](https://github.com/Grupo9-ITBootcampMeli/pi-ml-warehouse) services running with their default ports, hostnames in the same network.

## Authors
- [Amanda Zara](https://github.com/azfernandes)
- [André Veziane](https://github.com/andrevezi)
- [Antônio Schappo](https://github.com/antonio-schappo)
- [Guilherme Pereira](https://github.com/GuiSilva23)
- [Joan Silva](https://github.com/joanmeli)
- [Vinicius Brito](https://github.com/ViniCBrito)
