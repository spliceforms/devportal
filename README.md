# readme-custom-login

## Intrdocution

This project demosnstrates how to login to readme.io via a developer portal.

The site is a single html page, where you enter the details of the readme project and the user that you want to login, and proceed to get a JWT that is then accepted by readme.io to login the user.

Refer to [readme's documentation](https://docs.readme.com/main/docs/custom-login-page) for details.

The site is deployed [here.](https://devportal.apibanking.dev)

## Local Setup

Clone the repository, and run
`quarkus dev`.

## Deploy

1. First build the container
   `quarkus build -Dghcr`.
2. Deploy using kamal `VERSION=latest dotenv kamal setup -P`
