# 1. Берем легкий образ с Java 17 (официальный Eclipse Temurin)
FROM eclipse-temurin:17-jdk-focal

# 2. Устанавливаем версию SBT, которую будем использовать
ENV SBT_VERSION=1.11.7

# 3. Устанавливаем curl и bash
RUN apt-get update && \
    apt-get install -y curl bash && \
    rm -rf /var/lib/apt/lists/*

# 4. Скачиваем и устанавливаем sbt
RUN curl -L -o sbt-$SBT_VERSION.deb https://repo.scala-sbt.org/scalasbt/debian/sbt-$SBT_VERSION.deb && \
    dpkg -i sbt-$SBT_VERSION.deb && \
    rm sbt-$SBT_VERSION.deb

# 5. Создаем рабочую папку
WORKDIR /app

# 6. Копируем файлы описания зависимостей
COPY build.sbt .
COPY project project

# 8. Копируем весь остальной код (src)
COPY src ./src

# 9. Точка входа: запускаем тест через sbt gatling:testOnly
ENTRYPOINT ["sbt"]
CMD ["gatling:test"]
