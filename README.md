# Web Application Template

This projects is a template of a web application that has an integration with Auth0 as security provider and Spring Security to handle authorization at the appliation level, the intend is to reduce the friction of initial configuration.

## Dependencies

This application is built on top of (but not limited to):

* Spring Boot 3.1
* Spring Security 6
* Java 17
* Spring Web
* Thymeleaf
* Auth0

## Configuration

In order to execute the application there are some configuration that is required.

### Auth0

In Auth0 management UI it is required to create a new Application of type *Regular Web Applications*, after creating it, the following information is needed to be defined as a OS environment variable

* `AUTH0_CLIENT_ID`: Client ID
* `AUTH0_CLIENT_SECRET`: Client Secret
* `AUTH0_DOMAIN`: Domain

#### Application Properties

It is required to set the application URLs properly

Allowed Callback URLs: The callback configured for different environments every URL should be separated by comma, we should concatenate `/login/oauth2/code/auth0` to the base application URL.

Allowed Logout URLs: The URL where auth provider will redirect after performing logout, it can be the application's base URL. Consider adding the URLs of the different environments separated by comma.

#### Add defined roles as user claims

In order to let web application validate user roles we need to add the assigned roles in the user claims in the Login Flow.

1. Create a custom action that will trigger when Login / Post Login with the following code:

```
exports.onExecutePostLogin = async (event, api) => {

  const namespace = 'net.cavitos.app';
  
  if (event.authorization) {

    api.idToken.setCustomClaim(`${namespace}.roles`, event.authorization.roles);
    api.accessToken.setCustomClaim(`${namespace}.roles`, event.authorization.roles);
  }  
};
```

2. Add custom action into Login flow


