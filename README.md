# ShortYoutube
_by Harsh Goyal_

**IMPORTANT -** To run short youtube application you should have to go google developer to enable this API. After that make API Key to run this and replace the string in
`strings.xml`

Replaceable string - `ENTER API KEY`

Below are some of the videos that you can relate how the app is working ->

**Splash Activity & Main Activity -** Splash activity is used to show the name of the application that take 2 seconds. After that Main Activity opens that uses shimmer effect 
that you can see in loading, app only works when internet connection is available and shows the preference list items that provided with the help of grid view. There is seperate
`activity_main` file for the landscape layout.

https://user-images.githubusercontent.com/79085857/141681745-a2f530cb-9e8f-47d3-b9cd-9aff5dc4311e.mp4

**Custom Activity -** We can also say that it is the DetailActivity that takes two fragments which is Detail Fragment and Videos Fragment. Detail Fragment shows the data from the
youtube content creator and Video Fragment shows the latest video uploaded by the user on Youtube.

https://user-images.githubusercontent.com/79085857/141682565-4085f1c9-e99e-49c5-87ac-592a05684d4b.mp4

**Search Activity -** This activity focus on search the query that user enter. It also don't work if internet connection is not available. If the user change the mobile 
view (landscape) it automatically search the query without user interaction with the application.

https://user-images.githubusercontent.com/79085857/141682078-0aec40a8-806c-4547-a184-b4c1b270fdd6.mp4

**Settings Activity -** This activity uses shared preferences if the application is closed it saved the user preference and there should be some value provide to the channel list
and channel video list if it not it takes the previous value that was saved by the user.

https://user-images.githubusercontent.com/79085857/141682231-b5b862ee-391a-4239-8aeb-8190b67224f4.mp4
