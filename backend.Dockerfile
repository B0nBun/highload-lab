FROM gradle:8.14.3-jdk21-alpine AS build
WORKDIR /app
COPY . /app

USER root
RUN chown -R gradle /app
USER gradle

RUN echo 'POSTGRES_HOST=db' >> .env
RUN sed -E -i 's/localhost:5432/db:5432/' .env
RUN gradle bootJar -i

CMD sh ./docker-entry.sh