export const env = {
    API_BASE_URL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
    // Note: PORT is configured in vite.config.ts for the dev server. 
    // If you need it in the client, suffix with VITE_ and use import.meta.env.VITE_PORT
} as const;
