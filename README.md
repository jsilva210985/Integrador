# Integrador

![Java Version](https://img.shields.io/badge/Java-1.8-blue)
![Build Tool](https://img.shields.io/badge/Build-Maven-orange)
![Version](https://img.shields.io/badge/Version-2.0-green)

## Project Overview

**Integrador** is a robust Spring Boot-based middleware designed to facilitate seamless integration with logistics carriers, specifically focusing on **FedEx REST API** and **Estafeta**. It provides a standardized REST interface to handle complex shipping operations, pickup scheduling, tracking, and rate calculations, abstracting the underlying provider complexities for client applications.

---

## Key Features

- **Multi-Carrier Support**: Native integration with FedEx (REST API) and Estafeta.
- **Comprehensive Shipping Workflow**:
  - **Shipping (Label Generation)**: Automated label creation and database logging.
  - **Pickup Management**: Scheduling and management of package collection.
  - **Tracking**: Real-time package status updates.
  - **Rating/Quoting**: Dynamic service cost estimation.
- **Advanced FedEx Express Support**: Specialized handling for `FDXE` services, including Saturday pickup logic and building location details.
- **Carta Porte Integration**: Support for Mexican fiscal requirements (Carta Porte).
- **Extensible Architecture**: Modular service layer designed to incorporate new carriers or business rules easily.

---

## Installation & Requirements

### Prerequisites
- **Java Development Kit (JDK) 8**
- **Apache Maven 3.6+**
- **MySQL Database** (for configuration and logging persistence)

### Setup
1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd Integrador
   ```
2. Configure the database in `src/main/resources/application.properties`.
3. Build the project:
   ```bash
   ./mvnw clean install
   ```
4. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## Usage Examples

### 1. Authentication
The application uses a token-based validation layer synchronized with a local database (`validateAccess`). Ensure your request includes the mandatory credentials:

```json
{
  "Client": "YOUR_CLIENT_ID",
  "Password": "YOUR_PASSWORD",
  "Token": "YOUR_ACCESS_TOKEN"
}
```

### 2. FedEx Pickup Request (v1 REST)
**Endpoint:** `POST /Fedex/Rest/Pickup`

```json
{
  "Client": "demo",
  "Password": "...",
  "Token": "...",
  "OriginDetail": {
    "PickupLocation": {
      "Contact": {
        "PersonName": "KAREN G",
        "PhoneNumber": "1231231231"
      },
      "Address": {
        "StreetLines": "PADUA",
        "City": "TLAJOMULCO DE ZUNIGA",
        "StateOrProvinceCode": "JA",
        "PostalCode": "45650",
        "CountryCode": "MX"
      }
    },
    "PackageLocation": "NONE",
    "BuildingPart": "SUITE",
    "BuildingPartDescription": "394",
    "ReadyTimestamp": "2026-04-13T10:00:00",
    "CompanyCloseTime": "15:00:00"
  },
  "PackageCount": 1,
  "TotalWeight": {
    "Units": "KG",
    "Value": 1.0
  },
  "CarrierCode": "FDXE"
}
```

---

## API / Integration Overview

| Feature | Endpoint | Description |
| :--- | :--- | :--- |
| **FedEx Ship** | `/Fedex/Rest/Ship` | Generates FedEx Express/Ground shipping labels. |
| **FedEx Pickup** | `/Fedex/Rest/Pickup` | Schedules a courier collection for Express packages. |
| **FedEx Track** | `/Fedex/Rest/Tracking` | Retrieves current status and history of a tracking number. |
| **FedEx Rate** | `/Fedex/Rest/Rate` | Provides cost estimates for different carrier services. |
| **Carta Porte** | `/Fedex/Rest/CartaPorte` | Handles specialized fiscal documentation for transport. |
| **Estafeta** | `/Estafeta/...` | Legacy and REST integration for Estafeta services. |

---

## Configuration

The application relies on a dynamic attribute system stored in the database. Core parameters are fetched via `AtributoService`:

- **Sandbox API:** `https://apis-sandbox.fedex.com`
- **Carrier Credentials:** Managed via `account` and `key` attributes in the `atributo` table, allowing multiple accounts and environments without code changes.

---

## Project Structure

- `com.integrador.restcontroller`: Entry points for RESTful requests.
- `com.integrador.services`: Core business logic and carrier abstraction.
- `com.integrador.carriers`: Specific implementations for FedEx and Estafeta APIs.
- `com.integrador.models`: Entity definitions for persistence.
- `com.integrador.repositories`: JPA repositories for database access.

---

## Author Section

**Name:** Jose de Jesus Silva Ambrosio  
**Email:** jsilva210985@gmail.com  
**Documentation Date:** 2026-04-10

---