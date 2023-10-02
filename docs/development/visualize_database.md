# Visualize database

1. Start Docker Desktop
2. Run the app on an emulator
3. Open Device Explorer in Android Studio 
4. Copy `/data/data/io.github.lydavid.musicsearch.debug/databases/mbjc.db` to project root
5. Run the following with Powershell

```shell
docker run `
--mount type=bind,source="${PWD}",target=/home/schcrwlr `
--rm -it `
schemacrawler/schemacrawler `
/opt/schemacrawler/bin/schemacrawler.sh `
--server=sqlite `
--database=musicsearch.db `
--info-level=standard `
--command=schema `
--output-file=assets/musicsearch_db_schema.svg
```

https://dev.to/sualeh/how-to-visualize-your-sqlite-database-with-one-command-and-nothing-to-install-1f4m
