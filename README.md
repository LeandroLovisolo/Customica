# Custómica #

This is the source code for Custómica, my defunct startup ([customica.com](http://customica.com)).

Custómica was an online t-shirt store for the Argentinian market where anyone could design their own t-shirt right from their browser.

It failed to gain any significant traction, so I'm releasing its source code. The application is still running in read-only mode on a free Heroku dyno. You can check it out [here](http://customica.com).

## Requirements ##

### Play framework ###

Custómica is a [Play](http://www.playframework.org/) application written in Java. You need to [download](http://www.playframework.org/download) Play version **1.2.4** in order to run the app.

### A PostgreSQL database ###

Custómica is currently running on Heroku using a shared PostgreSQL database, so you need a local instance of PostgreSQL to run it on your computer.

### Facebook App ID/secret ###

This webapp uses Facebook as its login system. For it to work you need a valid Facebook app ID/secret. You can register a new Facebook app [here](https://developers.facebook.com/).

## How to run it ##

You can start the app by cd-ing into the app directory and executing `play run` (assuming you've added the Play installation directory to your system PATH), but you need to define a few environment variables first:

* `DATABASE_URL`: URL for your PostgreSQL database (e.g. postgres://username:password@hostname:port/database)
* `FACEBOOK_ID`: Your Facebook app ID
* `FACEBOOK_SECRET`: Your Facebook app secret
* `ADMIN_USER`: Your desired username for the administrative dashboard (details below)
* `ADMIN_PASS`: Password for the administrative dashboard
* `READ_ONLY`: Whether the app is running in read-only mode or not. Possible values: true, false

You can easily do this in one line:

```
play run -DDATABASE_URL=postgres://<user>:<pass>@<host>:<port>/<db> -DFACEBOOK_ID=<your app ID> \
-DFACEBOOK_SECRET=<your app secret> -DREAD_ONLY=<true or false> \
-DADMIN_USER=<dashboard username> -DADMIN_PASS=<dashboad password>
```

The app will be available at http://localhost:9000/ after running the command above.

If you're starting with an empty database, then the first thing you should do is create a default tshirt category from the administrative dashboard as explained below. If you omit this step, your users won't be able to save their custom-designed tshirts.

Note that you need to run the app from the same domain that you've specified when you registered your Facebook application, otherwise the login system won't work. You can edit your /etc/hosts file as explained [here](http://leandro.me/posts/facebook-apps-and-etc-hosts/).

## Administrative dashboard ##

You can log into the administrative dashboard by going to `<your app url>/admin` and entering the username/password combo that you've specified in the `ADMIN_USER` and `ADMIN_PASS` environment variables.

From the administrative dashboard you can create tshirt categories, edit/remove tshirts created by the users and review your orders.

### Default tshirt category ###

If you're starting with an empty database, go to `<your app url>/admin/crud/categories/new` and create a new default category for your tshirts. Don't forget to check the "isDefault" option.

## Read-only mode ##

Every time a user creates a new design, the application generates a couple of PNG images that are stored in the app's public/designs directory and used to display the new tshirt design across the website.

Custómica used to run from a paid VPS before going belly up, but now I've moved it to Heroku where it's running on a free dyno (Cedar stack).

Given the way Heroku works, it's not possible to persist data in the filesystem, so instead of rewriting the code responsible of rendering and storing the tshirt images, I decided to just code a configuration switch to run the app in read-only mode and check some of the existing designs into the Git repository (images in `public/designs` were originally ignored by Git.)

If you want to run Custómica on Heroku, remember to set the `READ_ONLY` environment variable to true.
