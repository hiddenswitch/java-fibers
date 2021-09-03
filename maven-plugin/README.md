# Quasar Ahead of Time Instrumentation Plugin

From https://github.com/vy/quasar-maven-plugin

### Usage

Add the following to your POM file:

```xml
<plugin>
    <groupId>com.hiddenswitch.fibers</groupId>
    <artifactId>maven-plugin</artifactId>
    <version>1.0.0</version>
    <configuration>
        <check>true</check>
        <debug>true</debug>
        <verbose>true</verbose>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>instrument</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```