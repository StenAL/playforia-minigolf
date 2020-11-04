# Track File Format
Tracks use special file format where each line is in a format of `<char> <content>` where `char` is character specifying type of the line and content is the actual data.

## V1 File Format
This format is now outdated and does not contain `TrackCategory`.
To parse this file format you can use, now deprecated, `TrackFileParser`.

```
V 1
A {AUTHOR OF TRACK}
N {NAME OF TRACK}
T {DATA}
I {NUMBER OF PLAYERS TO COMPLETE},{NUMBER OF STROKES},{BEST NUMBER OF STROKES},{NUMBER OF PEOPLE THAT GOT BEST STROKE}
B {FIRST BEST PAR PLAYER},{UNIX TIMESTAMP OF FIRST BEST PAR}
L {LAST BEST PAR PLAYER},{UNIX TIMESTAMP OF LAST BEST PAR}
R {RATING: 0},{RATING: 1},{RATING: 2},{RATING: 3},{RATING: 4},{RATING: 5},{RATING: 6},{RATING: 7},{RATING: 8},{RATING: 9},{RATING: 10}
```
### Directory structure
Since this format does not have `TrackCategory` specified in the actual file it
determines it based on the directory it is located in.
That means all `modern` tracks needs to be in `tracks/modern` directory and so on.
If you want track in multiple categories you need to duplicate the file and put it in multiple directories.

## V2+ File Format
New format that has `TrackCategory` specified inside the file. It can be parsed with `VersionedTrackFileParser`
```
V >=2
A {AUTHOR OF TRACK}
N {NAME OF TRACK}
T {DATA}
C {CATEGORY_ID}, {CATEGORY_Id}, ...
I {NUMBER OF PLAYERS TO COMPLETE},{NUMBER OF STROKES},{BEST NUMBER OF STROKES},{NUMBER OF PEOPLE THAT GOT BEST STROKE}
B {FIRST BEST PAR PLAYER},{UNIX TIMESTAMP OF FIRST BEST PAR}000
L {LAST BEST PAR PLAYER},{UNIX TIMESTAMP OF LAST BEST PAR}000
R {RATING: 0},{RATING: 1},{RATING: 2},{RATING: 3},{RATING: 4},{RATING: 5},{RATING: 6},{RATING: 7},{RATING: 8},{RATING: 9},{RATING: 10}
```

### Directory Structure
All tracks are located in `tracks/tracks` directory and tracksets are in `tracks/sets`


