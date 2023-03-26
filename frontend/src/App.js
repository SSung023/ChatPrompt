import './App.css';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import Root from './pages/Root';
import Login from './pages/Login';
import EditInst from './pages/EditInst';
import EditInput from './pages/EditInput';
import InquireInst from './pages/InquireInst';
import InquireIo from './pages/InquireIo';
import PrivateRoute from './utility/routes/PrivateRoute';

const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login/>
  },
  {
    path: '/',
    element: <PrivateRoute component={<Root />}/>,
    // element: <Root />,
    children: [
      { index: true, element: <EditInst /> } ,
      { path: '/input', element: <EditInput />},
      { path: '/inquire/instruction', element: <InquireInst />},
      { path: '/inquire/io', element: <InquireIo />},
    ]
  }
]);

function App() {
  return (
    <div>
      <RouterProvider router={router}/>
    </div>
  );
}

export default App;