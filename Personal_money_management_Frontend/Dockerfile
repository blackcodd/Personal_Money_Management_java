# Stage 1: Build React App
FROM node:20-alpine AS build
WORKDIR /app

# Dependencies ইনস্টল করা
COPY package*.json ./
RUN npm ci

# Source code কপি করে Production Build তৈরি করা
COPY . .
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine

# Vite Build-এর আউটপুট (dist) Nginx-এর ওয়েব ফোল্ডারে কপি করা
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]