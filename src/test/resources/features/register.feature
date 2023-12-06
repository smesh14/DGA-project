Feature: register user

  Scenario: Call backend to create new user
    When the client calls endpoint "/api/user/create" with body
     """
    {
        "phoneNumber": "123",
        "email": "string",
        "password": "12",
        "name": "sa",
        "role": "USER"
    }
    """
    Then response status code is 200