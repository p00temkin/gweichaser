## GweiChaser

Launch this tool and get mobile notifications (Pushover, https://pushover.net/) when gas fees are low/at your specified threshold. 

![alt text](https://github.com/p00temkin/gweichaser/blob/master/img/g.png?raw=true)

### Prerequisites

   ```
git clone https://github.com/p00temkin/forestfish
mvn clean package install
   ```

### Building the application

   ```
   mvn clean package
   mv target/gweichaser-0.0.1-SNAPSHOT-jar-with-dependencies.jar gweichaser.jar
   ```
   
### Usage

Using an Infura ETH Provider URL https://mainnet.infura.io/v3/abc, a pushover UserID A and AppID B with GWEI threshold of 200 and repeatcount 2. 

   ```
   java -jar ./gweichaser.jar c ETHEREUM -p "https://mainnet.infura.io/v3/abc" -u A -a B -t 200 -r 2
   ```
   
Options:
   ```
 -c,--chain <arg>                   EVM chain shortname (ETHEREUM, POLYGON, ..)
 -a,--apitokenappid <arg>           API token app ID
 -p,--providerurl <arg>             ETH Provider URL (infura etc)
 -r,--repeatbelowthreshold <arg>    repeat below threshold (default 2)
 -s,--pollintervalinseconds <arg>   poll interval in seconds (default 60)
 -t,--gweithreshold <arg>           GWEI threshold (default 20)
 -u,--apitokenuserid <arg>          API token user ID
   ```
   
### Support/Donate

forestfish.x / 0x207d907768Df538F32f0F642a281416657692743
   
