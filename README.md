keeps-characterization-txt
==========================

Characterization tool for text files, made by KEEP SOLUTIONS.


## Build & Use

To build the application simply clone the project and execute the following Maven command: `mvn clean package`  
The binary will appear at `target/txt-characterization-tool-1.0-SNAPSHOT-jar-with-dependencies.jar`

To see the usage options execute the command:

```bash
$ java -jar target/txt-characterization-tool-1.0-SNAPSHOT-jar-with-dependencies.jar -h
usage: java -jar [jarFile]
 -f <arg> file to analyze
 -h       print this message
 -v       print this tool version
 -c <arg> minimum confidence in charset detection
 -a       accepted charset (separated with ,)
```

## Tool Output Example
```bash
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<textCharacterizationResult>
    <validationInfo>
        <valid>false</valid>
        <validationError>The file have an acceptable encoding, but the confidence is too low (19 &lt; 50)</validationError>
    </validationInfo>
    <features/>
</textCharacterizationResult>


<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<textCharacterizationResult>
    <validationInfo>
        <valid>true</valid>
    </validationInfo>
    <features>
        <item>
            <key>numberOfLines</key>
            <value>282</value>
        </item>
        <item>
            <key>charset</key>
            <value>ISO-8859-1</value>
        </item>
    </features>
</textCharacterizationResult>
```

## License

This software is available under the [LGPL version 3 license](LICENSE).

