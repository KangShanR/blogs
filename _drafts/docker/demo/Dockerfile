FROM node:14
# create app directory
WORKDIR /usr/src/app
# install app dependencies
# a wildcard is used to ensure both package.json and package-lock.json are copied where available   
COPY package*.json ./
RUN npm install

# Boundle app source
COPY . .
EXPOSE 8080

CMD ["node", "server.js"]
