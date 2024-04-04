package com.seong.shoutlink.fixture;

public final class ApiFixture {

    public static int DEFAULT_FIXED_TAG_COUNT = 3;

    public static String geminiResponse() {
        return """
                  {
                  "candidates": [
                    {
                      "content": {
                        "parts": [
                          {
                            "text": "태그1,태그2,태그3"
                          }
                        ],
                        "role": "model"
                      },
                      "finishReason": "STOP",
                      "index": 0,
                      "safetyRatings": [
                        {
                          "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                          "probability": "NEGLIGIBLE"
                        },
                        {
                          "category": "HARM_CATEGORY_HATE_SPEECH",
                          "probability": "NEGLIGIBLE"
                        },
                        {
                          "category": "HARM_CATEGORY_HARASSMENT",
                          "probability": "NEGLIGIBLE"
                        },
                        {
                          "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
                          "probability": "NEGLIGIBLE"
                        }
                      ]
                    }
                  ],
                  "promptFeedback": {
                    "safetyRatings": [
                      {
                        "category": "HARM_CATEGORY_SEXUALLY_EXPLICIT",
                        "probability": "NEGLIGIBLE"
                      },
                      {
                        "category": "HARM_CATEGORY_HATE_SPEECH",
                        "probability": "NEGLIGIBLE"
                      },
                      {
                        "category": "HARM_CATEGORY_HARASSMENT",
                        "probability": "NEGLIGIBLE"
                      },
                      {
                        "category": "HARM_CATEGORY_DANGEROUS_CONTENT",
                        "probability": "NEGLIGIBLE"
                      }
                    ]
                  }
                }
                                """;
    }
}
