## GWEICHASER

Launch this tool and get mobile notifications (Pushover, <https://pushover.net/>) when gas fees are low/at your specified threshold.

![alt text](https://github.com/p00temkin/gweichaser/blob/master/img/gweichase.png?raw=true)

### Prerequisites

[Java 17+, Maven 3.x]

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

Using a random Ethereum chain node with GWEI threshold of 15

   ```
   java -jar ./gweichaser.jar -c ETHEREUM -t 15
   ```

Using a personal Infura Ethereum Provider URL <https://mainnet.infura.io/v3/abc>, a pushover UserID A and AppID B with GWEI threshold of 10 and repeatcount 2

   ```
   java -jar ./gweichaser.jar -c ETHEREUM -p "https://mainnet.infura.io/v3/abc" -u A -a B -t 10 -r 2
   ```

Using a optmized Polygon chain node selection with GWEI threshold of 15. Since the response time for all known Polygon providerURLs are evaluated the script initialization will introduce a slight delay.

   ```
   java -jar ./gweichaser.jar -c POLYGON -t 15 -n
   ```

Options:

   ```
 -c,--chain <arg>                   EVM chain shortname (ETHEREUM, POLYGON, ..)
 -a,--apitokenappid <arg>           API token app ID
 -p,--providerurl <arg>             ETH Provider URL (infura etc)
 -r,--repeatbelowthreshold <arg>    repeat below threshold (default 3)
 -s,--pollintervalinseconds <arg>   poll interval in seconds (default 60)
 -t,--gweithreshold <arg>           GWEI threshold (default 20)
 -u,--apitokenuserid <arg>          API token user ID
 -n,--nodeopt <arg>                 Instruct to use optimized node/providerURL selection
   ```

### Support/Donate

To support this project directly:

   ```
   Ethereum/EVM: forestfish.x / 0x207d907768Df538F32f0F642a281416657692743
   Algorand: forestfish.x / 3LW6KZ5WZ22KAK4KV2G73H4HL2XBD3PD3Z5ZOSKFWGRWZDB5DTDCXE6NYU
   ```

Or please consider donating to EFF:
[Electronic Frontier Foundation](https://supporters.eff.org/donate)
