import Socket from 'simple-websocket';

let _socket;

export const initRoomSocket = (dispatch, user) => {
  const uri = `ws://${window.location.host}/ws/room/?user=${user}`;
  const socket = Socket(uri);
  socket.on('data', (payload) => {
    const data = new TextDecoder("utf-8").decode(payload);
    if (data === 'keep alive')
      return;
    let json;
    try {
      json = JSON.parse(data);
    } catch(e) {
      console.log(`Response not JSON: ${data}`);
    }
    json && dispatch(json)
  });
  _socket = socket
};

export const emit = (payload) => _socket.send(payload);
