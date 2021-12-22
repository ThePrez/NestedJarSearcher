# NestedJarSearcher
A simple tool to search a jar/zip and all inner jar/zip files for a target filename.

This tool will also print out Log4J versions located in any jar/zip files where there are hits for the target file.

# To install
- Download the .zip from the releases page
- unzip it on the filesystem (use `java -jar` if you have to)
- run the `jarsearch` script

# Usage examples (assumes installation dir is $HOME)
Find all .war files in the current directory and search for JndiLookup.class:
```
find . -name \*.war -print -exec $HOME/jarsearch {} JndiLookup.class \;
```
Grep for `'\*\*\*'` to only get information about "hits" printed out


# Disclaimer
As stated in [the license](LICENSE), this software is provided on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND.

Also note that this software is contributed on a personal basis. It is not dlivered,
vetted, or endorsed by IBM in any way.
