# Deadline

Modify this file to satisfy a submission requirement related to the project
deadline. Please keep this file organized using Markdown. If you click on
this file in your GitHub repository website, then you will see that the
Markdown is transformed into nice-looking HTML.

## Part 1.1: App Description

> Please provide a friendly description of your app, including
> the primary functions available to users of the app. Be sure to
> describe exactly what APIs you are using and how they are connected
> in a meaningful way.

> **Also, include the GitHub `https` URL to your repository.**

My app takes user input of a minimum price, maximum price, minimum metacritic score,
and currency type, and generates a list of Steam games that fit the criteria.
The top of the list has games that fit the criteria and are currently discounted,
while the options toward the bottom are not discounted (but still fit the user's
criteria).

Repo URL: https://github.com/JohnJames415/cs1302-api-app

## Part 1.2: APIs

> For each RESTful JSON API that your app uses (at least two are required),
> include an example URL for a typical request made by your app. If you
> need to include additional notes (e.g., regarding API keys or rate
> limits), then you can do that below the URL/URI. Placeholders for this
> information are provided below. If your app uses more than two RESTful
> JSON APIs, then include them with similar formatting.

### API 1

```
https://www.cheapshark.com/api/1.0/deals?storeID=1&upperPrice=15&lowerPrice=5
```

### API 2

```
https://api.api-ninjas.com/v1/convertcurrency?have=USD&want=EUR&amount=5000
```

This API requires an API key (and mine is listed below). The API key does not go in
the URL, but instead is send as part of the HttpRequest (using
.header("X-Api-Key", API_KEY) when making the object).
Here is my API Key: ld2YNdZSjRnKAsMxzaxrfw==HKnxy4J95zXmNJYy

## Part 2: New

> What is something new and/or exciting that you learned from working
> on this project?

I learned how powerful APIs can be when you put them together. It creates opportunties  for
such a wide variety of useful and fun applications that otherwise would not have been possible.

## Part 3: Retrospect

> If you could start the project over from scratch, what do
> you think might do differently and why?

I would adjust the UI to be more clear and less crammed. Other than that, I would go back and adjust
some of the areas for user input, as I feel like there might have been some useful search criteria
that I did not create options for.
