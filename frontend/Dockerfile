# Usar la imagen de Nginx
FROM nginx:alpine

# Copiar los archivos compilados localmente al directorio de Nginx
COPY dist/dental-moovi /usr/share/nginx/html

# Copiar la configuración de Nginx
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Comando por defecto para ejecutar Nginx
CMD ["nginx", "-g", "daemon off;"]