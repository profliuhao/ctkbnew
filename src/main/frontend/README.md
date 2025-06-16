# CTKB Frontend

Modern Vue.js 3 frontend for the Clinical Trial Knowledge Base application.

## Technology Stack

- **Vue.js 3** with Composition API
- **Vite** for fast development and building
- **Vue Router 4** for client-side routing
- **Pinia** for state management
- **Element Plus** for UI components
- **Chart.js** for data visualization
- **Axios** for HTTP requests

## Development Setup

### Prerequisites

- Node.js 18+ and npm
- Java 17+ (for backend)

### Quick Start

1. **Install dependencies:**
   ```bash
   cd src/main/frontend
   npm install
   ```

2. **Start development server:**
   ```bash
   npm run dev
   ```
   The frontend will be available at `http://localhost:3000`

3. **Start backend server:**
   ```bash
   # In project root
   mvn spring-boot:run
   ```
   The backend API will be available at `http://localhost:8081`

### Available Scripts

- `npm run dev` - Start development server with hot reload
- `npm run build` - Build for production
- `npm run preview` - Preview production build locally
- `npm run lint` - Run ESLint

## Project Structure

```
src/main/frontend/
├── src/
│   ├── api/           # API service layer
│   ├── components/    # Reusable Vue components
│   ├── router/        # Vue Router configuration
│   ├── stores/        # Pinia stores
│   ├── views/         # Page components
│   ├── App.vue        # Root component
│   └── main.js        # Application entry point
├── index.html         # HTML template
├── package.json       # Dependencies and scripts
└── vite.config.js     # Vite configuration
```

## Features

### Dashboard
- Overview statistics and metrics
- Quick search functionality
- Recent activity feed
- Quick action buttons

### Conditions
- Browse and search clinical trial conditions
- Advanced filtering options
- Pagination and sorting
- Detailed condition view

### Criteria
- Explore clinical trial criteria
- Relationship analysis between conditions and criteria
- Domain-based filtering
- Statistical insights

### Analytics
- Data visualizations and charts
- Trend analysis over time
- Top conditions and criteria
- Domain distribution charts

### Search
- Multi-tab search results
- Search across conditions, measurements, drugs, and procedures
- Export functionality
- Advanced filtering

## API Integration

The frontend communicates with the Spring Boot backend through a well-defined API layer:

- **Conditions API**: `/api/condition/*`
- **Common Conditions API**: `/api/common-condition/*`
- **Criteria API**: `/api/all-criteria/*`
- **Measurements API**: `/api/measurement/*`
- **Drugs API**: `/api/drug/*`
- **Procedures API**: `/api/procedure/*`

## Build Integration

The frontend is integrated with the Maven build process using the Frontend Maven Plugin:

1. **Development**: Run `npm run dev` for hot reload development
2. **Production**: Run `mvn clean package` to build both frontend and backend
3. **Deployment**: The built frontend assets are served by Spring Boot

## Configuration

### Development Proxy

The Vite development server is configured to proxy API requests to the Spring Boot backend:

```javascript
// vite.config.js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8081',
      changeOrigin: true,
      rewrite: (path) => path.replace(/^\/api/, '')
    }
  }
}
```

### Production Build

The production build outputs to `../resources/static/dist/` so Spring Boot can serve the static files.

## Contributing

1. Follow Vue.js 3 Composition API patterns
2. Use Element Plus components for consistency
3. Implement proper error handling
4. Add loading states for better UX
5. Follow the established project structure
6. Write meaningful commit messages

## Troubleshooting

### Common Issues

1. **Port conflicts**: Change the dev server port in `vite.config.js`
2. **API connection issues**: Ensure the Spring Boot backend is running on port 8081
3. **Build failures**: Clear `node_modules` and reinstall dependencies
4. **CORS issues**: Check that the backend has proper CORS configuration

### Performance Tips

1. Use lazy loading for routes
2. Implement virtual scrolling for large datasets
3. Optimize images and assets
4. Use proper caching strategies
5. Monitor bundle size with `npm run build`
