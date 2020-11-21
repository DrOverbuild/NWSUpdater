# RESTful API
Note that `https://nwsupdater.com` is not our own domain, and we use it here as an example.

## Quick URLS:
A summary of all the URLs used. Descriptions are down below.
* Logging in: `GET https://nwsupdater.com/auth`
* Logging out: `GET https://nwsupdater.com/logout`
* New account creation: `POST https://nwsupdater.com/user`
* Edit profile: `GET https://nwsupdater.com/user`
* Save changes to profile: `UPDATE https://nwsupdater.com/user`
* Load home page: `GET https://nwsupdater.com/`
* New location: `POST https://nwsupdater.com/location`
* Edit location: `GET https://nwsupdater.com/location/{location_id}`
* Save changes to location: `UPDATE https://nwsupdater.com/location/{location_id}`
* Delete location: `DELETE https://nwsupdater.com/location/{location_id}`
* Get all alert types: `GET https://nwsupdater.com/alerts`

## Error message
To any request that the server does not know how to process, we will respond with this example payload:
```json
{
  "error": {
    "code": 1,
    "msg": "Not logged in"
  }
}
```

## Logging in
Users will user their email to log in.

With a request from `https://nwsupdater.com/` with no session/user id, return with error message noting that the user
is not logged in. At this point, the browser will display a log in screen. When clicking submit, the browser will send
this GET request:

```
GET https://nwsupdater.com/auth
```

Request body:
```json
{
  "user": {
    "email": "jreddin1@cub.uca.edu",
    "password": "password"
  }
}
```

For security reasons we do not want to put the parameters of this request in its URL, so we will put it in the body
instead. A GET request can have a body. Upon a successful login, in addition to setting the session's 
`logged_in_user_id` variable, the server will respond with this response body, containing the user information:

```json
{
  "user": {
    "id": 1,
    "email": "jreddin1@cub.uca.edu",
    "phone": "501-514-3557"
  }
}
```

If the log in was not succesful, return with error as documented above.

## New Account Creation
When clicking submit from the create new account screen, the browser will send this POST request:

```
POST https://nwsupdater.com/user
```

Request body:
```json
{
  "user": {
    "email": "jreddin1@cub.uca.edu",
    "phone": "501-514-3557",
    "password": "password"
  }
}
```

Note that the request body will not have a `verify` attribute because verification will happen on the client side.

If something went wrong, send an error message, otherwise, respond with the newly created user:
```json
{
  "user": {
    "id": 1,
    "email": "jreddin1@cub.uca.edu",
    "phone": "501-514-3557"
  }
}
```

## After log in complete
Sessions will be manually implemented, hoping the client keeps track of the "token" we send it (which in this case
is simply a UUID generated at time of log in), and will return it to the server when logged in operations take
place through the `Authorization: Bearer` HTTP header.

Any request below will respond with a 'user not logged in' error if there is no valid authorization header.

Once the authorization checks, save this current session, and respond with this JSON payload, 
containing a list of the user's locations, and the user itself:

```json
{
  "locations": [
    {
      "id": 1,
      "name": "Miami, FL"
    },
    {
      "id": 2,
      "name": "Conway, AR"
    },
    ...
  ],
  "user": {
    "id": 1,
    "email": "jreddin1@cub.uca.edu"
  },
  "sessionToken": "00000-0000000-00000"
}
```

Even though each location as well as the user has more attributes than what is shown above, we will only use locations'
id and name and the user's id and email on the home page.

## Editing Profile
When the user clicks on 'Edit Profile' on the top right, the browser will send this request:

```
GET https://nwsupdater.com/user
```

The server should use the session's `logged_in_user_id` to respond with the user's information:
```json
{
  "user": {
    "id": 1,
    "email": "jreddin1@cub.uca.edu",
    "phone": "501-514-3557"
  }
}
```

The user will update information, and on hitting 'Save', the browser will send this request:
```
UPDATE https://nwsupdater.com/user
```

Request body:
```json
{
  "user": {
    "email": "jreddin1@cub.uca.edu",
    "phone": "501-514-3557",
    "password": "password"
  }
}
```

## Location Configuration Page
Edit Location and New Location will have the same UI. The only difference here is that upon Edit Location, a request for the
full location data is sent.

## Edit Location
The browser will make this request:
```
GET https://nwsupdater.com/locations/{location_id}
```

The server will respond with this body:
```json
{
  "location": {
    "id": 1,
    "name": "Miami, FL",
    "lat": -80.0000,
    "lon": 26.0000,
    "smsEnabled": true,
    "emailEnabled": true,
    "alerts": [
      {
        "id": 1,
        "name": "Tornado Warning"
      },
      {
        "id": 2,
        "name": "Tornado Watch"
      },
      ...
    ],
    "ownerID": 1
  }
}
```
Note that the server will need to validate that the `logged_in_user_id` matches the location's `ownerID`.

After hitting save, the browser will send this request:
```
UPDATE https://nwsupdater.com/locations/{location_id}
```
With this body:
```json
{
  "location": {
    "id": 1,
    "name": "Miami, FL",
    "lat": -80.0000,
    "lon": 26.0000,
    "smsEnabled": true,
    "emailEnabled": true,
    "alerts": [
      {
        "id": 1,
        "name": "Tornado Warning"
      },
      {
        "id": 2,
        "name": "Tornado Watch"
      },
      ...
    ],
    "ownerID": 1
  }
}
```

The server will respond with the location data or an error message if something went wrong.


## Loading alerts
In addition to needing location information, the browser will run a second request to load all the alerts that are
available in order to display a table. The browser will run this request every time this page is loaded, weather the
user is creating a new location or editing an existing one.
```
GET https://nwsupdater.com/alerts
```

And the browser will respond with this body:
```json
{
  "alerts": [
    {
      "id": 1,
      "name": "Tornado Warning"
    },
    {
      "id": 2,
      "name": "Tornado Watch"
    },
    ...
  ]
}
```

## Adding a New Location
The browser will send this request after the user hits 'Save'

```
POST https://nwsupdater.com/locations/
```

With this body:
```json
{
  "location": {
    "name": "Miami, FL",
    "lat": -80.0000,
    "lon": 26.0000,
    "smsEnabled": true,
    "emailEnabled": true,
    "alerts": [
      {
        "id": 1,
        "name": "Tornado Warning"
      },
      {
        "id": 2,
        "name": "Tornado Watch"
      },
      ...
    ]
  }
}
```

The server will respond with the location or an error if something went wrong.

## Deleting a Location
The browser will send this request if the user deletes a location:
```
DELETE https://nwsupdater.com/locations/{location_id}
```
The server will respond with a list of the user's locations:

```json
{
  "locations": [
    {
      "id": 1,
      "name": "Miami, FL"
    },
    {
      "id": 2,
      "name": "Conway, AR"
    },
    ...
  ]
}
```

## Signing Out
Upon signing out, the browser will send this request:
```
GET https://nwsupdater.com/logout
```

When this happens, the server will clear the session.