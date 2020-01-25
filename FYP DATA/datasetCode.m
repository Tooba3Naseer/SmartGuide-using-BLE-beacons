MacAddresses = {'F8:05:E6:24:C2:7B', 'DE:C7:66:22:9B:89', 'D0:BC:C4:91:DC:BE', 'EC:08:57:69:6C:86' ,'E5:73:ED:F0:A9:43', 'EE:24:B1:D3:12:69', 'CE:50:E4:EC:F1:0E', 'E8:C5:00:06:63:72', 'D5:9E:76:EE:30:EE','C9:BE:18:AB:D2:C2', 'DB:B0:66:C0:09:1E', 'C0:EC:E5:67:99:2C', 'F1:C1:E2:D9:72:72', 'C1:2E:33:08:C8:E3','D8:27:F1:B8:B7:D0', 'CB:CB:F5:92:A0:46', 'C0:EF:2C:82:C9:B8', 'C1:1C:6F:26:31:EA', 'FC:C6:B9:93:86:AA','ED:2C:C3:6B:E6:7B', 'E9:C6:45:C2:07:B8', 'EB:B7:16:73:62:E6', 'C2:6C:15:0C:C1:C7', 'D4:7F:9D:18:E7:6A'}; % contains mac addresses of all beacons
Files=dir('C:\Users\HP 820\Desktop\DATACS'); % give directory of the folder that contains all csv files
RSSI_values = [];
MacAdd =[];
cellarray_data = cell(1,1);   % declare cell array(where each cell contains any type of data) 
counter=1;
folder = 'C:\Users\HP 820\Desktop\DATACS';


for k=3:(length(Files)-2)      
   FileName = Files(k).name; % read file name
   fullFileName = fullfile(folder, FileName);
   T = readtable(fullFileName,'FileType', 'spreadsheet', 'ReadVariableNames', true);  % fetch data from csv file and store in table form
   iter1 = 5;
   while FileName(iter1) ~= '_'
       iter1 = iter1+1;
   end
    
   v = T.DEVICE_NAME;  % fetch row values of device_name column from table
   m = T.MAC_ADDRESS;  % fetch row values of mac_adddress column from table
   if ~isempty(m)
   MacAdd = [MacAdd, m(1)];
   for i=1:(length(v)/2)
       RSSI_values = [RSSI_values, v(2*i)]; % get RSSI values, eliminate null values and store in vector
       if i~=(length(v)/2)
           MacAdd = [MacAdd, m(2*i+1)];  % get mac address, eliminate null values and store in vector
       end
   end
   for f=1:length(RSSI_values)
       for j=1:length(MacAddresses)
           if isequal(MacAdd(f),MacAddresses(j)) % compare mac addresses of all scanned devices to mac address of beacons
               cellarray_data(counter,j) = RSSI_values(f); % store in cell array only RSSI values of beacons
           end
       end
   end
   cellarray_data(counter,(length(MacAddresses)+1)) = cellstr(FileName(5:(iter1-1))); % add room label value in cell array
   % cellstr convert string into cell
   counter=counter+1;
   end
   RSSI_values = [];
   MacAdd =[];
end
[r c] = size(cellarray_data);
for i=1:r
    for j=1:length(MacAddresses)
        if isempty(cellarray_data{i,j}) % check if any cell is empty
            a = cellfun(@str2double,cellarray_data(i,1:length(MacAddresses)));
            cellarray_data(i,j) = cellstr(int2str(100)); % replace empty value with max of RSSI values +5, 
            %as RSSI values are in negative so max value of +ve values is basically min or weak signal strength
        end
    end
end
for i=(1:r)
     a = cellfun(@str2double,cellarray_data(i,1:length(MacAddresses))); % convert cell array to vector
     cellarray_data(i,1:length(MacAddresses)) = num2cell(a*-1); % negate all RSSI values,convert vector to cell array 
end
T = cell2table(cellarray_data(1:end,:),'VariableNames', {'MacAddr1','MacAddr2','MacAddr3','MacAddr4','MacAddr5','MacAddr6','MacAddr7','MacAddr8','MacAddr9','MacAddr10','MacAddr11','MacAddr12','MacAddr13','MacAddr14', 'MacAddr15','MacAddr16','MacAddr17','MacAddr18','MacAddr19','MacAddr20','MacAddr21','MacAddr22','MacAddr23','MacAddr24','RoomLabel'}); % convert cell array into table
writetable(T,'dataset.csv') % write into csv file


