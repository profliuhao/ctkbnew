#!/bin/bash

# CTKB Frontend Setup Script
echo "🚀 Setting up CTKB Frontend..."

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 18+ first."
    echo "   Visit: https://nodejs.org/"
    exit 1
fi

# Check Node.js version
NODE_VERSION=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
if [ "$NODE_VERSION" -lt 18 ]; then
    echo "❌ Node.js version 18+ is required. Current version: $(node -v)"
    exit 1
fi

echo "✅ Node.js version: $(node -v)"

# Navigate to frontend directory
cd src/main/frontend

# Install dependencies
echo "📦 Installing dependencies..."
npm install

if [ $? -eq 0 ]; then
    echo "✅ Dependencies installed successfully!"
else
    echo "❌ Failed to install dependencies"
    exit 1
fi

# Create .env file if it doesn't exist
if [ ! -f .env ]; then
    echo "📝 Creating .env file..."
    cat > .env << EOL
# Development environment variables
VITE_API_BASE_URL=http://localhost:8081
VITE_APP_TITLE=Clinical Trial Knowledge Base
EOL
    echo "✅ .env file created"
fi

echo ""
echo "🎉 Frontend setup complete!"
echo ""
echo "📋 Next steps:"
echo "   1. Start the backend: mvn spring-boot:run"
echo "   2. Start the frontend: cd src/main/frontend && npm run dev"
echo "   3. Open http://localhost:3000 in your browser"
echo ""
echo "🔧 Available commands:"
echo "   npm run dev     - Start development server"
echo "   npm run build   - Build for production"
echo "   npm run preview - Preview production build"
echo "   npm run lint    - Run linter"
echo ""
echo "📚 For more information, see src/main/frontend/README.md"
