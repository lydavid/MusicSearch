# Local MusicBrainz

## Run MusicBrainz mirror on your own machine
Just follow the instructions from https://github.com/metabrainz/musicbrainz-docker.
On Windows, I followed them within WSL.

## Connect to WSL2 server on local network

https://stackoverflow.com/a/66485783

> **Notice:** I set the **connectaddress** to **localhost** not to the IP address of the WSL because by default the requests that go to localhost are forwarded to the WSL. By doing this you won't need to set the port forwarding every time you restart your machine because the IP address of the WSL is dynamic.


Run PowerShell as Administrator (Windows + S, search PowerShell).

```bash
netsh advfirewall firewall add rule name="Allowing LAN connections" dir=in action=allow protocol=TCP localport=5000
```

```bash
netsh interface portproxy add v4tov4 listenport=5000 listenaddress=0.0.0.0 connectport=5000 connectaddress=localhost
```

This assumes our server is running on port 5000. For our use case, docker-compose is running MusicBrainz on this port, so it works.

Afterwards, on our Android phone, or any other devices on the same network,
we can browse MusicBrainz in the browser with `10.0.0.211:5000`. Though CSS seems to be broken.
We can also make webservice requests with Termux:
```
curlie 10.0.0.211:5000/ws/2/artist?query=a
```


## Inspect and stop (PowerShell)

```
netsh interface portproxy show v4tov4
```

```
netsh int portproxy reset all
```


## Connecting MusicSearch to this local instance 

If I just swap out `https://musicbrainz.org` for `http://10.0.0.211:5000`, the app will crash, and I will see something like this:
```
REQUEST http://10.0.0.211:5000/ws/2/artist?query=b&limit=100&offset=0 failed with exception: java.net.UnknownServiceException: CLEARTEXT communication to 10.0.0.211 not permitted by network security policy
2023-12-10 10:07:58.856  6876-6876  LoggingMod...gingModule io.github.lydavid.musicsearch.debug  D  REQUEST: http://10.0.0.211:5000/ws/2/artist?query=b&limit=100&offset=0
                                                                                                    METHOD: HttpMethod(value=GET)
                                                                                                    COMMON HEADERS
                                                                                                    -> Accept: application/json
                                                                                                    -> Accept-Charset: UTF-8
                                                                                                    -> User-Agent: MusicSearch (https://github.com/lydavid/MusicSearch)
                                                                                                    CONTENT HEADERS
                                                                                                    -> Content-Length: 0
```

One way to turn this to HTTPS is to deploy it with ngrok.
I tried out [Linux](https://dashboard.ngrok.com/get-started/setup/linux) from inside WSL,
but it was slow.
Also tried with Docker Desktop on Windows, running it from Git Bash, but that was also slow.

For now, I will just allow CLEARTEXT for specific domains listed in `network_security_config.xml`.
Anyone else who wish to connect to their own MusicBrainz mirror running on their local network,
will have to edit that file with the local IP address of the machine running it.
