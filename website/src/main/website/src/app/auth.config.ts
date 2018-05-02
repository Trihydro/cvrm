// Licensed to the Apache Software Foundation (ASF) under one or more contributor 
// license agreements; and to You under the Apache License, Version 2.0.

interface AuthConfiguration {
    clientID: string,
    domain: string,
    callbackURL: string
}

export const myConfig: AuthConfiguration = {
    clientID: '{CLIENT_ID}',
    domain: '{DOMAIN}',
    // You may need to change this!
    callbackURL: 'http://localhost:3000/'
};