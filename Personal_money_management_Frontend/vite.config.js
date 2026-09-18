import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api': 'http://localhost:8080',
      '/totals': 'http://localhost:8080',
      '/category-percentages': 'http://localhost:8080',
      '/filterTransactions': 'http://localhost:8080',
      '/budget': 'http://localhost:8080',
      '/expenseinperiod': 'http://localhost:8080'
    }
  }
});
