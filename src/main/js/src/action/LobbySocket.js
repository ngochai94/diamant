import Socket from 'simple-websocket';
import {initRoomSocket} from './RoomSocket';

let _socket;

export const init = (dispatch, user) => {
  const uri = `ws://${window.location.host}/ws/lobby?user=${user}`;
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

    if (json && json.currentRoom && json.currentRoom.started) {
      console.log('open websocket for room');
      initRoomSocket(dispatch, user);
    }

    json && dispatch(json);
  });
  _socket = socket
};

export const emit = (payload) => _socket.send(payload);
