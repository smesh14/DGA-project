Feature: login

  Scenario: successful login attempt
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
    When the client calls endpoint "/api/user/login" with body
     """
    {
        "password": "12",
        "userName": "sa"
    }
    """
    Then response status code is 200

