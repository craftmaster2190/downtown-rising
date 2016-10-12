Commands

npm install
npm run watch-dev
npm build

mvn spring-boot:run -Drun.profiles=dev
mvn package

cf push downtown -p target/downtown-0.0.1-SNAPSHOT.jar