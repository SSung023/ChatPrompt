import './App.css';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import Root from './pages/Root';
import Login from './pages/Login';
import EditInst from './pages/EditInst';
import EditInput from './pages/EditInput';
import InquireInst from './pages/InquireInst';
import InquireIo from './pages/InquireIo';
import PrivateRoute from './utility/routes/PrivateRoute';
import EditInstPro from './pages/EditInstPro';
import Inspect from './pages/Inspect';
import DuplicatedInst from './pages/DuplicatedInst';

const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login/>
  },
  {
    path: '/',
    element: <PrivateRoute component={<Root />}/>,
    children: [
      { index: true, element: <EditInst /> } ,
      { path: '/input', element: <EditInput />},
      { path: '/inquire/instruction', element: <InquireInst />},
      { path: '/inquire/io', element: <InquireIo />},
      { path: '/inspect', element: <Inspect />},
      { path: '/duplicated/instruction', element: <DuplicatedInst />},
      { path: '/duplicated/io', element: <DuplicatedInst />},
      { path: '/admin', element: <EditInstPro />},
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