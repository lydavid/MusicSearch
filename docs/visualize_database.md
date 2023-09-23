# Visualize database

1. Run the app on an emulator
2. Open Device Explorer in Android Studio 
3. Copy `/data/data/io.github.lydavid.musicsearch.debug/databases/mbjc.db` to project root
4. Run the following with Powershell

```shell
docker run `
--mount type=bind,source="${PWD}",target=/home/schcrwlr `
--rm -it `
schemacrawler/schemacrawler `
/opt/schemacrawler/bin/schemacrawler.sh `
--server=sqlite `
--database=mbjc.db `
--info-level=standard `
--command=schema `
--output-file=assets/mbjc_db_schema.svg
```

https://dev.to/sualeh/how-to-visualize-your-sqlite-database-with-one-command-and-nothing-to-install-1f4m
