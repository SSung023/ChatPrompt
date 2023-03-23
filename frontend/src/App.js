import './App.css';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import Root from './pages/Root';
import EditInst from './pages/EditInst';
import EditInput from './pages/EditInput';
import Login from './pages/Login';
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