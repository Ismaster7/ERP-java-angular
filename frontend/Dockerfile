# eu escolhi buildar toda vez para facilitar minhas alterações,
# mas podem remover este trecho após o primeiro build.
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production


FROM nginx:1.27-alpine

COPY --from=build /app/dist/frontend /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

