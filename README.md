# Clinical Trial Knowledge Base (CTKB)

A comprehensive web application for analyzing and visualizing clinical trial eligibility criteria data. This system provides advanced analytics, search capabilities, and data visualizations for clinical trial conditions, criteria, and related medical concepts.

## 🏗️ Architecture

**Full-Stack Application:**
- **Backend**: Spring Boot 3.2.1 with Java 17
- **Frontend**: Vue.js 3 with Composition API, Element Plus UI
- **Database**: MySQL with MyBatis-Plus ORM
- **Search**: Elasticsearch integration
- **Caching**: Redis for performance optimization
- **Build**: Maven with integrated frontend build process

## 📊 Features

### Core Functionality
- **Conditions Management**: Browse and analyze clinical trial conditions with detailed statistics
- **Criteria Analysis**: Comprehensive eligibility criteria data with inclusion/exclusion tracking
- **Advanced Analytics**: Trial timeline analysis, recruitment patterns, and predictive insights
- **Search & Filtering**: Powerful search capabilities across conditions, criteria, and trials
- **Data Visualizations**: Interactive charts and graphs for data insights

### Technical Features
- **RESTful API**: Well-structured API endpoints for all data operations
- **Real-time Analytics**: Dynamic dashboard with live statistics
- **Responsive Design**: Modern UI that works across devices
- **Performance Optimized**: Pagination, caching, and optimized queries
- **API Documentation**: Swagger/OpenAPI integration

## 🚀 Quick Start

### Prerequisites

- **Java 17+** (OpenJDK or Oracle JDK)
- **Node.js 18+** and npm
- **MySQL 8.0+**
- **Redis** (optional, for caching)
- **Elasticsearch** (optional, for advanced search)
- **Maven 3.6+**

### 1. Database Setup

```sql
-- Create database
CREATE DATABASE ctkb_new CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Import schema and data
mysql -u root -p ctkb_new < src/sql/ctkb.sql
```

### 2. Configuration

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost/ctkb_new?useUnicode=true&characterEncoding=UTF-8
spring.datasource.username=your_username
spring.datasource.password=your_password

# Redis Configuration (optional)
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=your_redis_password

# Elasticsearch Configuration (optional)
spring.elasticsearch.uris=http://localhost:9200
spring.elasticsearch.username=elastic
spring.elasticsearch.password=your_es_password
```

### 3. Frontend Setup

Run the automated setup script:

```bash
chmod +x setup-frontend.sh
./setup-frontend.sh
```

Or manually:

```bash
cd src/main/frontend
npm install
```

### 4. Development Mode

**Terminal 1 - Backend:**
```bash
mvn spring-boot:run
```

**Terminal 2 - Frontend:**
```bash
cd src/main/frontend
npm run dev
```

**Access the application:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8081
- API Documentation: http://localhost:8081/swagger-ui.html
- Database Monitor: http://localhost:8081/druid (admin/admin)

### 5. Production Build

```bash
# Build everything
mvn clean package

# Run production build
java -jar target/ctkbnew-0.0.1-SNAPSHOT.jar
```

## 📁 Project Structure

```
ctkbnew/
├── src/
│   ├── main/
│   │   ├── java/org/haoai/medixhub/ctkb/
│   │   │   ├── controller/          # REST API controllers
│   │   │   ├── service/             # Business logic services
│   │   │   ├── dao/                 # Data access objects
│   │   │   ├── entity/              # JPA entities
│   │   │   ├── config/              # Spring configuration
│   │   │   └── CtkbnewApplication.java
│   │   ├── frontend/                # Vue.js frontend application
│   │   │   ├── src/
│   │   │   │   ├── components/      # Vue components
│   │   │   │   ├── views/           # Page components
│   │   │   │   ├── stores/          # Pinia state management
│   │   │   │   ├── router/          # Vue Router configuration
│   │   │   │   └── utils/           # Utility functions
│   │   │   ├── package.json
│   │   │   └── vite.config.js
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/dist/         # Built frontend assets
│   ├── sql/
│   │   └── ctkb.sql                 # Database schema and data
│   └── test/                        # Test files
├── target/                          # Build output
├── pom.xml                          # Maven configuration
├── setup-frontend.sh                # Frontend setup script
└── README.md
```

## 🔌 API Endpoints

### Core APIs
- **Conditions**: `/api/condition/*` - Clinical trial conditions
- **Criteria**: `/api/all-criteria/*` - Eligibility criteria
- **Common Conditions**: `/api/common-condition/*` - Aggregated condition data
- **Analytics**: `/api/analytics/*` - Advanced analytics and insights
- **Statistics**: `/api/statistics/*` - Statistical data and metrics

### Key Endpoints
```
GET  /api/condition/page/all                    # Get all conditions (paginated)
GET  /api/condition/concept_id/{id}             # Get condition by concept ID
GET  /api/common-condition/page/all             # Get common conditions
GET  /api/analytics/trial-timeline              # Trial timeline analysis
GET  /api/statistics/overview                   # Dashboard statistics
GET  /swagger-ui.html                           # API documentation
```

## 🗄️ Database Schema

### Core Tables
- **`ec_condition`** - Clinical trial conditions
- **`ec_drug`** - Drug-related criteria
- **`ec_measurement`** - Measurement criteria with numeric values
- **`ec_procedure`** - Procedure-related criteria
- **`ec_observation`** - Observation criteria
- **`ec_all_criteria`** - Unified criteria view
- **`ec_common_condition`** - Aggregated condition statistics

### Key Relationships
- Conditions linked to trials via `nctid` (ClinicalTrials.gov identifier)
- Criteria categorized by domain (Condition, Drug, Measurement, etc.)
- Inclusion/exclusion flags for eligibility criteria
- Concept mapping to standardized medical vocabularies

## 🛠️ Development

### Backend Development
```bash
# Run with hot reload
mvn spring-boot:run

# Run tests
mvn test

# Generate code coverage
mvn jacoco:report
```

### Frontend Development
```bash
cd src/main/frontend

# Development server with hot reload
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

## 📦 Deployment

### Docker Deployment (Recommended)

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/ctkbnew-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Traditional Deployment

1. **Build the application:**
   ```bash
   mvn clean package
   ```

2. **Deploy the JAR file:**
   ```bash
   java -jar target/ctkbnew-0.0.1-SNAPSHOT.jar
   ```

3. **Configure reverse proxy (Nginx):**
   ```nginx
   server {
       listen 80;
       server_name your-domain.com;
       
       location / {
           proxy_pass http://localhost:8081;
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
       }
   }
   ```

## 🔧 Configuration

### Environment Variables
```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ctkb_new
DB_USERNAME=root
DB_PASSWORD=password

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=password

# Elasticsearch
ES_HOST=localhost
ES_PORT=9200
ES_USERNAME=elastic
ES_PASSWORD=password
```

### Production Configuration
- Enable SSL/TLS
- Configure proper database connection pooling
- Set up monitoring and logging
- Configure backup strategies
- Implement security measures

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ConditionControllerTest

# Run frontend tests
cd src/main/frontend
npm test
```

## 📈 Performance

### Optimization Features
- **Database**: Connection pooling with Druid
- **Caching**: Redis for frequently accessed data
- **Pagination**: Efficient data loading with MyBatis-Plus
- **Frontend**: Lazy loading and code splitting
- **API**: Response compression and caching headers

### Monitoring
- Druid database monitoring: `/druid`
- Spring Boot Actuator endpoints
- Application metrics and health checks

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Commit changes: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Submit a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **Hao Liu** - Initial development and architecture

## 🆘 Support

For support and questions:
- Create an issue in the GitHub repository
- Check the API documentation at `/swagger-ui.html`
- Review the frontend documentation in `src/main/frontend/README.md`

---

**Note**: This application is designed for research and educational purposes in clinical trial data analysis.
