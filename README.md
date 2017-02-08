Commands

npm install
npm start
npm run build

gulp bump

mvn spring-boot:run -Drun.profiles=dev
mvn package

cf login -a api.run.pivotal.io
cf push downtown -p target/downtown-0.0.1-SNAPSHOT.jar

mvn clean install package && cf target -s development && cf push downtown -p $(ls -t target/downtown*.jar | head -1)
 
mvn clean install package && cf target -s production && cf push downtown -p $(ls -t target/downtown*.jar | head -1)

