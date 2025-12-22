# 1. 자바와 메이븐이 설치된 컴퓨터를 빌려온다
FROM maven:3.8.5-openjdk-17

# 2. 작업할 폴더를 만든다
WORKDIR /app

# 3. 내 프로젝트 파일들을 다 복사해 넣는다
COPY . .

# 4. 빌드한다 (테스트는 건너뛴다)
RUN mvn clean package -DskipTests

# 5. 완성된 프로그램을 실행한다
# (주의: wisdom-0.0.1-SNAPSHOT.jar 이름이 pom.xml과 같아야 함)
CMD ["java", "-jar", "target/wisdom-0.0.1-SNAPSHOT.jar"]