Feature: login


  Scenario: add contact
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

    When the client calls endpoint "/api/user/add/contact/sa" with body
     """
    {
      "name": "gio",
      "phoneNumber": "123"
    }
    """

    When the client calls endpoint "/api/user/load/contact/sa"

    Then response status code is 200


