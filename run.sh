#!/bin/bash

DOTENV=.env
if [ ! -z $1 ]; then
  DOTENV=$1
fi

echo Exporting environment variables from $DOTENV...

if [ -f $DOTENV ]; then
  # Export the vars in .env into shell
  export $(egrep -v '^#' $DOTENV | xargs)
  echo ... done exporting environmnent variables

else
  echo ... WARNING! File does not exist
fi

java -jar application/target/desktop-service.jar server configuration.yml
