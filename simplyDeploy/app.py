from flask import Flask, jsonify, request

app = Flask(__name__)

from pandora.openai.auth import Auth0


def share_token_config(username, password):
    try:
        proxy = ''
        print(f'Login begin: {username}')
        try:
            token = Auth0(username, password, proxy).auth(False)
            print(f'Login success: {username}')
            print(token)
        except Exception as e:
            err_str = str(e).replace('\n', '').replace('\r', '').strip()
            print(f'Login failed: {username}, {err_str}')
            token = err_str
        return token
    except Exception as e:
        print(f"An error occurred: {str(e)}")
        return None


@app.route('/get-token', methods=['POST'])
def get_token():
    data = request.get_json()

    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        return jsonify({'error': 'Username and password are required'})

    token = share_token_config(username, password)

    if token:
        return jsonify({'token': token})
    else:
        return jsonify({'error': 'Failed to obtain Token'})


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=8082)

