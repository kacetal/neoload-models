# Rendezvous policy
The Rendezvous policy to be applied on the already defined Rendezvous in the User Path.

#### Available settings

| Name                                                   | Description                                   | Accept variable | Required | Since |
|:------------------------------------------------------ |:--------------------------------------------- |:---------------:|:--------:|:-----:|
| name                                                   | The name of the Rendezvous.                   | -               | &#x2713; |  7.6  |
| when                                                   | When to release the Rendezvous. Possible values are: "manual", percentage or positive number.| -               | &#x2713;        |  7.6  |
| timeout                                                | The timeout between Virtual Users. Timeout duration is expressed in hours (h), minutes (m), seconds (s). | -          | -        |  7.6  |

#### Example

Defining the Rendezvous policy for the Scenario:
If "When" is "manual", use a Javascript action to free waiting Virtual Users. Otherwise, release when percentage or positive number of Virtual Users arrive at the Rendezvous point.
```yaml
rendezvous_policies:
  - name: rendezvous1
    when: manual
    timeout: 30s
  - name: rendezvous2
    when: 50%
    timeout: 2m
  - name: rendezvous3
    when: 10
    timeout: 1h
```
