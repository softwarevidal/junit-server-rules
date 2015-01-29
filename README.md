# JUnit server rules

## Embedded JUnit Tomcat 8.x rule

Just use it like this:

```java
	@ClassRule
	public static final ServletContainerRule server = new ServletContainerRule() {
		@Override
		public Iterable<WebAppParameter> webappParameters() {
			return ImmutableList.of(
				parameter("spring.profiles.active", "atom,apostil,multi-lang,news")
         		);
		}

      		@Override
     	 	public String contextPath() {
         		return "api";
      		}
   	};
```

You can then have access to `server.uri()` and do things like:

```java
	Request createRequest = new Request.Builder()
        	.url(resolve("my/relative/uri")) // yields (e.g.) : http://localhost:52379/api/my/relative/uri
         	.build();
	// [...]

	private URL resolve(String relativeUrl) throws MalformedURLException {
		return server.uri().resolve(relativeUrl).toURL();
	}
```
