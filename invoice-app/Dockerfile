FROM node:12.2.0
WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json /app/package.json
RUN npm install
COPY . /app
CMD ng serve --host 0.0.0.0 --port 4200